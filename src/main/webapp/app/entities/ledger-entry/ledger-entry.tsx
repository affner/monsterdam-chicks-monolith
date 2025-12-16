import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './ledger-entry.reducer';

export const LedgerEntry = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const ledgerEntryList = useAppSelector(state => state.ledgerEntry.entities);
  const loading = useAppSelector(state => state.ledgerEntry.loading);
  const totalItems = useAppSelector(state => state.ledgerEntry.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="ledger-entry-heading" data-cy="LedgerEntryHeading">
        <Translate contentKey="monsterdamChicksApp.ledgerEntry.home.title">Ledger Entries</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="monsterdamChicksApp.ledgerEntry.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/ledger-entry/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="monsterdamChicksApp.ledgerEntry.home.createLabel">Create new Ledger Entry</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {ledgerEntryList && ledgerEntryList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="monsterdamChicksApp.ledgerEntry.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('amount')}>
                  <Translate contentKey="monsterdamChicksApp.ledgerEntry.amount">Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('amount')} />
                </th>
                <th className="hand" onClick={sort('currency')}>
                  <Translate contentKey="monsterdamChicksApp.ledgerEntry.currency">Currency</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('currency')} />
                </th>
                <th className="hand" onClick={sort('entryType')}>
                  <Translate contentKey="monsterdamChicksApp.ledgerEntry.entryType">Entry Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('entryType')} />
                </th>
                <th className="hand" onClick={sort('accountType')}>
                  <Translate contentKey="monsterdamChicksApp.ledgerEntry.accountType">Account Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('accountType')} />
                </th>
                <th className="hand" onClick={sort('accountOwnerId')}>
                  <Translate contentKey="monsterdamChicksApp.ledgerEntry.accountOwnerId">Account Owner Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('accountOwnerId')} />
                </th>
                <th className="hand" onClick={sort('reason')}>
                  <Translate contentKey="monsterdamChicksApp.ledgerEntry.reason">Reason</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('reason')} />
                </th>
                <th className="hand" onClick={sort('referenceType')}>
                  <Translate contentKey="monsterdamChicksApp.ledgerEntry.referenceType">Reference Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('referenceType')} />
                </th>
                <th className="hand" onClick={sort('referenceId')}>
                  <Translate contentKey="monsterdamChicksApp.ledgerEntry.referenceId">Reference Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('referenceId')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="monsterdamChicksApp.ledgerEntry.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="monsterdamChicksApp.ledgerEntry.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('deletedDate')}>
                  <Translate contentKey="monsterdamChicksApp.ledgerEntry.deletedDate">Deleted Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('deletedDate')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {ledgerEntryList.map((ledgerEntry, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/ledger-entry/${ledgerEntry.id}`} color="link" size="sm">
                      {ledgerEntry.id}
                    </Button>
                  </td>
                  <td>{ledgerEntry.amount}</td>
                  <td>{ledgerEntry.currency}</td>
                  <td>
                    <Translate contentKey={`monsterdamChicksApp.LedgerEntryType.${ledgerEntry.entryType}`} />
                  </td>
                  <td>
                    <Translate contentKey={`monsterdamChicksApp.LedgerAccountType.${ledgerEntry.accountType}`} />
                  </td>
                  <td>{ledgerEntry.accountOwnerId}</td>
                  <td>
                    <Translate contentKey={`monsterdamChicksApp.LedgerReason.${ledgerEntry.reason}`} />
                  </td>
                  <td>{ledgerEntry.referenceType}</td>
                  <td>{ledgerEntry.referenceId}</td>
                  <td>
                    {ledgerEntry.createdDate ? <TextFormat type="date" value={ledgerEntry.createdDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{ledgerEntry.createdBy}</td>
                  <td>
                    {ledgerEntry.deletedDate ? <TextFormat type="date" value={ledgerEntry.deletedDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/ledger-entry/${ledgerEntry.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/ledger-entry/${ledgerEntry.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/ledger-entry/${ledgerEntry.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="monsterdamChicksApp.ledgerEntry.home.notFound">No Ledger Entries found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={ledgerEntryList && ledgerEntryList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default LedgerEntry;
