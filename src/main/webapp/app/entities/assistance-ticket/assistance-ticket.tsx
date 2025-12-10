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

import { getEntities } from './assistance-ticket.reducer';

export const AssistanceTicket = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const assistanceTicketList = useAppSelector(state => state.assistanceTicket.entities);
  const loading = useAppSelector(state => state.assistanceTicket.loading);
  const totalItems = useAppSelector(state => state.assistanceTicket.totalItems);

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
      <h2 id="assistance-ticket-heading" data-cy="AssistanceTicketHeading">
        <Translate contentKey="monsterdamChicksApp.assistanceTicket.home.title">Assistance Tickets</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="monsterdamChicksApp.assistanceTicket.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/assistance-ticket/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="monsterdamChicksApp.assistanceTicket.home.createLabel">Create new Assistance Ticket</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {assistanceTicketList && assistanceTicketList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="monsterdamChicksApp.assistanceTicket.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('subject')}>
                  <Translate contentKey="monsterdamChicksApp.assistanceTicket.subject">Subject</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('subject')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="monsterdamChicksApp.assistanceTicket.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="monsterdamChicksApp.assistanceTicket.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('type')}>
                  <Translate contentKey="monsterdamChicksApp.assistanceTicket.type">Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('type')} />
                </th>
                <th className="hand" onClick={sort('openedAt')}>
                  <Translate contentKey="monsterdamChicksApp.assistanceTicket.openedAt">Opened At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('openedAt')} />
                </th>
                <th className="hand" onClick={sort('closedAt')}>
                  <Translate contentKey="monsterdamChicksApp.assistanceTicket.closedAt">Closed At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('closedAt')} />
                </th>
                <th className="hand" onClick={sort('comments')}>
                  <Translate contentKey="monsterdamChicksApp.assistanceTicket.comments">Comments</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('comments')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="monsterdamChicksApp.assistanceTicket.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="monsterdamChicksApp.assistanceTicket.lastModifiedDate">Last Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="monsterdamChicksApp.assistanceTicket.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedBy')}>
                  <Translate contentKey="monsterdamChicksApp.assistanceTicket.lastModifiedBy">Last Modified By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                </th>
                <th className="hand" onClick={sort('userId')}>
                  <Translate contentKey="monsterdamChicksApp.assistanceTicket.userId">User Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('userId')} />
                </th>
                <th>
                  <Translate contentKey="monsterdamChicksApp.assistanceTicket.assignedAdmin">Assigned Admin</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="monsterdamChicksApp.assistanceTicket.report">Report</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="monsterdamChicksApp.assistanceTicket.documentsReview">Documents Review</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {assistanceTicketList.map((assistanceTicket, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/assistance-ticket/${assistanceTicket.id}`} color="link" size="sm">
                      {assistanceTicket.id}
                    </Button>
                  </td>
                  <td>{assistanceTicket.subject}</td>
                  <td>{assistanceTicket.description}</td>
                  <td>
                    <Translate contentKey={`monsterdamChicksApp.TicketStatus.${assistanceTicket.status}`} />
                  </td>
                  <td>
                    <Translate contentKey={`monsterdamChicksApp.TicketType.${assistanceTicket.type}`} />
                  </td>
                  <td>
                    {assistanceTicket.openedAt ? (
                      <TextFormat type="date" value={assistanceTicket.openedAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {assistanceTicket.closedAt ? (
                      <TextFormat type="date" value={assistanceTicket.closedAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{assistanceTicket.comments}</td>
                  <td>
                    {assistanceTicket.createdDate ? (
                      <TextFormat type="date" value={assistanceTicket.createdDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {assistanceTicket.lastModifiedDate ? (
                      <TextFormat type="date" value={assistanceTicket.lastModifiedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{assistanceTicket.createdBy}</td>
                  <td>{assistanceTicket.lastModifiedBy}</td>
                  <td>{assistanceTicket.userId}</td>
                  <td>
                    {assistanceTicket.assignedAdmin ? (
                      <Link to={`/platform-admin-user/${assistanceTicket.assignedAdmin.id}`}>{assistanceTicket.assignedAdmin.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {assistanceTicket.report ? (
                      <Link to={`/user-report/${assistanceTicket.report.id}`}>{assistanceTicket.report.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {assistanceTicket.documentsReview ? (
                      <Link to={`/identity-document-review/${assistanceTicket.documentsReview.id}`}>
                        {assistanceTicket.documentsReview.id}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/assistance-ticket/${assistanceTicket.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/assistance-ticket/${assistanceTicket.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/assistance-ticket/${assistanceTicket.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="monsterdamChicksApp.assistanceTicket.home.notFound">No Assistance Tickets found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={assistanceTicketList && assistanceTicketList.length > 0 ? '' : 'd-none'}>
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

export default AssistanceTicket;
