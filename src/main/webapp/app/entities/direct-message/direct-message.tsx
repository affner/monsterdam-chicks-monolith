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

import { getEntities } from './direct-message.reducer';

export const DirectMessage = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const directMessageList = useAppSelector(state => state.directMessage.entities);
  const loading = useAppSelector(state => state.directMessage.loading);
  const totalItems = useAppSelector(state => state.directMessage.totalItems);

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
      <h2 id="direct-message-heading" data-cy="DirectMessageHeading">
        <Translate contentKey="monsterdamChicksApp.directMessage.home.title">Direct Messages</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="monsterdamChicksApp.directMessage.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/direct-message/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="monsterdamChicksApp.directMessage.home.createLabel">Create new Direct Message</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {directMessageList && directMessageList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="monsterdamChicksApp.directMessage.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('messageContent')}>
                  <Translate contentKey="monsterdamChicksApp.directMessage.messageContent">Message Content</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('messageContent')} />
                </th>
                <th className="hand" onClick={sort('readDate')}>
                  <Translate contentKey="monsterdamChicksApp.directMessage.readDate">Read Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('readDate')} />
                </th>
                <th className="hand" onClick={sort('likeCount')}>
                  <Translate contentKey="monsterdamChicksApp.directMessage.likeCount">Like Count</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('likeCount')} />
                </th>
                <th className="hand" onClick={sort('isHidden')}>
                  <Translate contentKey="monsterdamChicksApp.directMessage.isHidden">Is Hidden</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isHidden')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="monsterdamChicksApp.directMessage.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="monsterdamChicksApp.directMessage.lastModifiedDate">Last Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="monsterdamChicksApp.directMessage.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedBy')}>
                  <Translate contentKey="monsterdamChicksApp.directMessage.lastModifiedBy">Last Modified By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                </th>
                <th className="hand" onClick={sort('isDeleted')}>
                  <Translate contentKey="monsterdamChicksApp.directMessage.isDeleted">Is Deleted</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isDeleted')} />
                </th>
                <th className="hand" onClick={sort('repliedStoryId')}>
                  <Translate contentKey="monsterdamChicksApp.directMessage.repliedStoryId">Replied Story Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('repliedStoryId')} />
                </th>
                <th className="hand" onClick={sort('senderId')}>
                  <Translate contentKey="monsterdamChicksApp.directMessage.senderId">Sender Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('senderId')} />
                </th>
                <th>
                  <Translate contentKey="monsterdamChicksApp.directMessage.responseTo">Response To</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="monsterdamChicksApp.directMessage.chatRoom">Chat Room</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {directMessageList.map((directMessage, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/direct-message/${directMessage.id}`} color="link" size="sm">
                      {directMessage.id}
                    </Button>
                  </td>
                  <td>{directMessage.messageContent}</td>
                  <td>
                    {directMessage.readDate ? <TextFormat type="date" value={directMessage.readDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{directMessage.likeCount}</td>
                  <td>{directMessage.isHidden ? 'true' : 'false'}</td>
                  <td>
                    {directMessage.createdDate ? (
                      <TextFormat type="date" value={directMessage.createdDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {directMessage.lastModifiedDate ? (
                      <TextFormat type="date" value={directMessage.lastModifiedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{directMessage.createdBy}</td>
                  <td>{directMessage.lastModifiedBy}</td>
                  <td>{directMessage.isDeleted ? 'true' : 'false'}</td>
                  <td>{directMessage.repliedStoryId}</td>
                  <td>{directMessage.senderId}</td>
                  <td>
                    {directMessage.responseTo ? (
                      <Link to={`/direct-message/${directMessage.responseTo.id}`}>{directMessage.responseTo.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {directMessage.chatRoom ? <Link to={`/chat-room/${directMessage.chatRoom.id}`}>{directMessage.chatRoom.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/direct-message/${directMessage.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/direct-message/${directMessage.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/direct-message/${directMessage.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="monsterdamChicksApp.directMessage.home.notFound">No Direct Messages found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={directMessageList && directMessageList.length > 0 ? '' : 'd-none'}>
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

export default DirectMessage;
